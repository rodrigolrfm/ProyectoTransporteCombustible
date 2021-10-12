import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { TransformWrapper, TransformComponent } from 'react-zoom-pan-pinch';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Button from '@material-ui/core/Button';
import Chip from '@material-ui/core/Chip';
import Tooltip from '@material-ui/core/Tooltip';
import StoreMallDirectoryIcon from '@material-ui/icons/StoreMallDirectory';
import DriveEtaIcon from '@material-ui/icons/DriveEta';
import TwoWheelerIcon from '@material-ui/icons/TwoWheeler';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import RefreshIcon from '@material-ui/icons/Refresh';
import IconButton from '@material-ui/core/IconButton';
import FiberManualRecordIcon from '@material-ui/icons/FiberManualRecord';
import { useHistory } from 'react-router-dom';
import { useSnackbar } from 'notistack';
import { MainContainer } from '../../components/main-container/main-container';
import { MainContent } from '../../components/main-content/main-content';
import { SubHeader } from '../../components/sub-header/sub-header';
import { VehicleCard } from '../../components/vehicle-card/vehicle-card';
import './index.scss';
import { VehicleDetail } from '../../components/vehicle-detail/vehicle-detail';
import { IState } from '../../store/reducers';
import { routeVehicleLoadAction } from '../../store/route-vehicle/actions/load.actions';
import { IRoute } from '../../interfaces/route.interface';
import { routesLoadActiveAction } from '../../store/routes/actions/load-active.actions';
import { deliverOrdersAction } from '../../store/orders/actions/deliver-orders.actions';
import { blockedNodesLoadAction } from '../../store/nodes/actions/load-blocked.actions';
import { getPositionAction } from '../../store/warehouse/actions/get-position.actions';
import { updateRouteStateAction } from '../../store/routes/actions/update-state.actios';
import { breakdownsLoadAction } from '../../store/breakdowns/actions/load-breakdowns.actions';
import { updateRouteBreakdownAction } from '../../store/routes/actions/update-breakdown.actions';

export interface IVehicleL {
  licensePlate: string;
  driver: string;
  vehicleType: string;
  routeNumber: number;
  state: string;
  qtyOrders: number;
  qtyDelivered: number;
  qtyToDeliver: number;
}

const initialVehiclePositions = [
  {
    id: 1,
    licensePlate: 'AZY-896',
    xPos: -5,
    yPos: 5,
    type: 1,
    break: 0,
    state: 0,
  },
];

export const Tracking = () => {
  const routes = useSelector((state: IState) => state.routes.activeRoutes);
  const blocked = useSelector((state: IState) => state.nodes.blocked);
  const routeVehicle = useSelector((state: IState) => state.routeVehicle.routeVehicle);
  const breakdowns = useSelector((state: IState) => state.breakdowns.breakdowns);
  const warehouse = useSelector((state: IState) => state.warehouse.warehouse);
  const dispatch = useDispatch();
  const history = useHistory();
  const { enqueueSnackbar } = useSnackbar();
  const [selected, setSelected] = useState('');
  const [showWarehouse, setShowWarehouse] = useState(true);
  const [showBlocked, setShowBlocked] = useState(true);
  const [isSelected, setIsSelected] = useState(false);
  const [vehiclePositions, setVehiclePositions] = useState(initialVehiclePositions);
  const distanceNode = 16;
  const carSpeed = 30;
  const motorcycleSpeed = 60;
  const ySize = 50;
  const breakTimeMinutes = 60; // TODO cambiar a 60

  useEffect(() => {
    dispatch(routesLoadActiveAction.request({}));
    dispatch(blockedNodesLoadAction.request({}));
    dispatch(getPositionAction.request({}));
    dispatch(breakdownsLoadAction.request({}));
  }, [dispatch]);

  const onClickReload = () => {
    dispatch(routesLoadActiveAction.request({}));
    dispatch(blockedNodesLoadAction.request({}));
    dispatch(getPositionAction.request({}));
    dispatch(breakdownsLoadAction.request({}));
  };

  const getCurrentPosition = (route: IRoute) => {
    // Evaluar si el empleado está en horario de descanso en base a la hora actual del sv
    // si está en descanso no ejecutar.
    // sino calcular la ruta normal
    // y cambiar estado de ruta a en pausa
    // evaluar si el conductor tuvo hora de descanso para quitar la hora extra.

    // Averías:
    // Evaluar si hay averias activas en un vehiculo, si hay, actualizar el estado del vehiculo
    // Se va a crear una nueva ruta y el nuevo vehiculo va a salir
    // Cuando el vehiculo llegue, se hace un llamado con el id de la ruta para que se actualice el estado y se borre del mapa
    // el nuevo vehiculo continua con su camino

    // Si hay averia se calcula la posición actual en base a la hora de la averia.

    const now = new Date();
    const beginDateTime = new Date(route.beginDateTime);
    const [hours, minutes, seconds] = route.employeeVehicle.employee.breaktime.split(':');
    const breakIniDateTime = new Date(
      now.getFullYear(),
      now.getMonth(),
      now.getDate(),
      Number(hours),
      Number(minutes),
      Number(seconds)
    );
    const nowTime = new Intl.DateTimeFormat('es-ES', {
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric',
    }).format(now);

    let breakIni = new Intl.DateTimeFormat('es-ES', {
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric',
    }).format(breakIniDateTime);

    if (breakIni.length === 7) {
      breakIni = `0${breakIni}`;
    }

    const breakEnd = new Intl.DateTimeFormat('es-ES', {
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric',
    }).format(
      new Date(
        now.getFullYear(),
        now.getMonth(),
        now.getDay(),
        Number(hours),
        Number(minutes) + breakTimeMinutes,
        Number(seconds)
      )
    );

    const beginTime = new Intl.DateTimeFormat('es-ES', {
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric',
    }).format(beginDateTime);

    let inBreak = 0;
    let lastBreakdown;
    let index = breakdowns.length - 1;
    for (; index >= 0; index -= 1) {
      if (breakdowns[index].vehicleId === route.employeeVehicle.vehicle.id) {
        lastBreakdown = breakdowns[index];
        break;
      }
    }

    let timeDiff = 0;
    if (breakIni < nowTime && nowTime < breakEnd) {
      inBreak = 1; // en break
      if (route.state.toString() === '1') {
        dispatch(updateRouteStateAction.request({ id: route.id, state: 0 }));
        dispatch(routesLoadActiveAction.request({}));
        enqueueSnackbar(
          `La ruta ${route.id} con ${
            route.employeeVehicle?.vehicle.type.id === 1 ? 'automóvil' : 'motocicleta'
          } de placa ${
            route.employeeVehicle?.vehicle.licensePlate
          } entró en descanso programado desde la hora ${
            route.employeeVehicle?.employee.breaktime
          }.`,
          { variant: 'warning', autoHideDuration: 5000, preventDuplicate: true }
        );
      }
    }

    if (beginTime < breakIni && nowTime >= breakEnd) {
      inBreak = 2; // tuvo break pero ya acabó
      if (route.state.toString() === '0') {
        dispatch(updateRouteStateAction.request({ id: route.id, state: 1 }));
        dispatch(routesLoadActiveAction.request({}));
        enqueueSnackbar(
          `La ruta ${route.id} con ${
            route.employeeVehicle?.vehicle.type.id === 1 ? 'automóvil' : 'motocicleta'
          } de placa ${
            route.employeeVehicle?.vehicle.licensePlate
          } finalizó su descanso programado.`,
          {
            variant: 'success',
            autoHideDuration: 5000,
            preventDuplicate: true,
          }
        );
      }
    }
    let breakdownDateTime = null;
    if (lastBreakdown) {
      breakdownDateTime = new Date(lastBreakdown.beginDate);
      const breakdownTime = new Intl.DateTimeFormat('es-ES', {
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric',
      }).format(breakdownDateTime);

      if (breakdownTime < nowTime && breakdownDateTime > beginDateTime) {
        if (inBreak === 0) {
          inBreak = 3; // BREAKDOWN O AVERÍA
        }
        if (inBreak === 2) {
          inBreak = 4;
        }

        if (route.state.toString() !== '4') {
          enqueueSnackbar(
            `${
              route.employeeVehicle?.vehicle.type.id === 1 ? 'El automóvil' : 'La motocicleta'
            } de placa ${route.employeeVehicle?.vehicle.licensePlate} de la ruta ${
              route.id
            } ha sufrido una avería.`,
            {
              variant: 'error',
              autoHideDuration: 5000,
              preventDuplicate: true,
            }
          );
          // notificar avería
          dispatch(updateRouteBreakdownAction.request({ id: route.id }));
          dispatch(routesLoadActiveAction.request({}));
        }
      }
    }

    switch (inBreak) {
      case 1: // En break
        timeDiff = (breakIniDateTime.getTime() - beginDateTime.getTime()) / 1000;
        break;
      case 2: {
        // tuvo break pero ya acabó
        const newNow = new Date(now.getTime());
        newNow.setMinutes(newNow.getMinutes() - breakTimeMinutes);
        timeDiff = (newNow.getTime() - beginDateTime.getTime()) / 1000;
        break;
      }
      case 3:
        timeDiff = (breakdownDateTime.getTime() - beginDateTime.getTime()) / 1000;
        break;
      case 4: {
        const newNow = new Date(breakdownDateTime.getTime());
        newNow.setMinutes(newNow.getMinutes() - breakTimeMinutes);
        timeDiff = (newNow.getTime() - beginDateTime.getTime()) / 1000;
        break;
      }
      default:
        timeDiff = (now.getTime() - beginDateTime.getTime()) / 1000;
        break;
    }

    const speedKmH = route.employeeVehicle?.vehicle.type.id === 1 ? carSpeed : motorcycleSpeed;
    const speedKmS = speedKmH / 3600;
    const totalDistance = route.nodes.length - 1;
    const distanceTraveled = timeDiff * speedKmS;
    const decimalDistanceTraveled = distanceTraveled % 1;
    const currentNodePosition = Math.floor(distanceTraveled);
    const currentNode = route.nodes[currentNodePosition];

    if (distanceTraveled > totalDistance) {
      return {
        id: route.id,
        xPos: route.nodes[route.nodes.length - 1].xCoord,
        yPos: route.nodes[route.nodes.length - 1].yCoord,
        licensePlate: route.employeeVehicle?.vehicle.licensePlate,
        type: route.employeeVehicle?.vehicle.type.id,
        break: inBreak,
        state: route.state,
      };
    }

    if (currentNodePosition === totalDistance) {
      return {
        id: route.id,
        xPos: currentNode.xCoord,
        yPos: currentNode.yCoord,
        licensePlate: route.employeeVehicle?.vehicle.licensePlate,
        type: route.employeeVehicle?.vehicle.type.id,
        break: inBreak,
        state: route.state,
      };
    }
    const nextNode = route.nodes[currentNodePosition + 1];
    const [currentXPos, currentYPos] = [
      nextNode.xCoord - currentNode.xCoord === 0
        ? nextNode.xCoord
        : currentNode.xCoord +
          (nextNode.xCoord > currentNode.xCoord
            ? decimalDistanceTraveled
            : -decimalDistanceTraveled),
      nextNode.yCoord - currentNode.yCoord === 0
        ? nextNode.yCoord
        : currentNode.yCoord +
          (nextNode.yCoord > currentNode.yCoord
            ? decimalDistanceTraveled
            : -decimalDistanceTraveled),
    ];

    const pickUpOrders = route.orders.find(
      item =>
        Math.abs(item.adress.xCoord - currentXPos) < 0.01 &&
        Math.abs(item.adress.yCoord - currentYPos) < 0.01 &&
        item.state === 10
    );
    if (pickUpOrders) {
      dispatch(updateRouteStateAction.request({ id: pickUpOrders.totalAmount, state: 5 }));
      dispatch(routesLoadActiveAction.request({}));
      enqueueSnackbar(
        `Los pedidos de la ruta averiada ${pickUpOrders.totalAmount} fueron recogidos por la ruta ${route.id}.`,
        { variant: 'success', autoHideDuration: 5000, preventDuplicate: true }
      );
    }

    const deliveredOrders = route.orders.filter(
      item =>
        Math.abs(item.adress.xCoord - currentXPos) < 0.01 &&
        Math.abs(item.adress.yCoord - currentYPos) < 0.01 &&
        item.state !== 10
    );
    if (deliveredOrders.length > 0) {
      dispatch(
        deliverOrdersAction.request({
          orders: deliveredOrders.map(item => {
            return item.id.toString();
          }),
        })
      );
      if (
        deliveredOrders
          .map(item => {
            return item.routeOrder === route.orders.length - 1;
          })
          .includes(false)
      ) {
        if (isSelected) {
          dispatch(routeVehicleLoadAction.request({ id: selected }));
        }

        enqueueSnackbar(
          `La entrega del pedido ${deliveredOrders[0].id} de la ruta ${route.id} ha finalizado satisfactoriamente.`,
          { variant: 'info', autoHideDuration: 5000, preventDuplicate: true }
        );
      } else {
        setIsSelected(false);
        dispatch(routesLoadActiveAction.request({}));
        enqueueSnackbar(
          `La ruta ${route.id} con ${
            route.employeeVehicle?.vehicle.type.id === 1 ? 'automóvil' : 'motocicleta'
          } de placa ${
            route.employeeVehicle?.vehicle.licensePlate
          } ha finalizado satisfactoriamente.`,
          { variant: 'success', autoHideDuration: 5000, preventDuplicate: true }
        );
      }
    }
    return {
      id: route.id,
      xPos: currentXPos,
      yPos: currentYPos,
      licensePlate: route.employeeVehicle?.vehicle.licensePlate,
      type: route.employeeVehicle?.vehicle.type.id,
      break: inBreak,
      state: route.state,
    };
  };

  useEffect(() => {
    if (isSelected) {
      const interval = setInterval(() => {
        const route = routes.find(item => item.id.toString() === selected);
        const positions = [getCurrentPosition(route)];
        setVehiclePositions(positions);
      }, 600);
      return () => clearInterval(interval);
    } else {
      const interval = setInterval(() => {
        const positions = routes.map(item => {
          return getCurrentPosition(item);
        });
        setVehiclePositions(positions);
      }, 600);
      return () => clearInterval(interval);
    }
  }, [routes, isSelected, selected]);

  useEffect(() => {
    const interval = setInterval(() => {
      dispatch(routesLoadActiveAction.request({}));
      dispatch(blockedNodesLoadAction.request({}));
      dispatch(breakdownsLoadAction.request({}));
    }, 30000);
    return () => clearInterval(interval);
  }, [routes]);

  const onClick = (item: string) => {
    setSelected(item);
    setIsSelected(true);
  };

  useEffect(() => {
    if (selected) {
      dispatch(routeVehicleLoadAction.request({ id: selected }));
    }
  }, [selected, isSelected]);

  const onClickDetail = () => {
    history.push(`routes/${routeVehicle?.id.toString()}/`);
  };

  const handleChangeWarehouse = () => {
    setShowWarehouse(!showWarehouse);
  };
  const handleChangeBlocked = () => {
    setShowBlocked(!showBlocked);
  };

  return (
    <MainContainer className="tracking">
      <MainContent>
        <SubHeader className="tracking" title="Seguimiento en tiempo real" />
        <div className="tracking__body">
          <Paper className="tracking__body__vehicles">
            {isSelected ? (
              <>
                <div className="tracking__body__vehicles__fixed">
                  <Typography variant="h6">Detalle de vehículo</Typography>
                  <Button
                    size="small"
                    startIcon={<ChevronLeftIcon />}
                    onClick={() => {
                      setIsSelected(false);
                      setSelected('');
                    }}
                  >
                    Volver a todos los vehículos
                  </Button>
                </div>
                <div className="tracking__body__vehicles__detail">
                  <VehicleDetail
                    onClick={onClickDetail}
                    key={routeVehicle?.licensePlate}
                    licensePlate={routeVehicle?.licensePlate}
                    driver={routeVehicle?.driver}
                    vehicleType={routeVehicle?.vehicleType}
                    routeNumber={routeVehicle?.id}
                    state={routeVehicle?.state.toString()}
                    qtyOrders={routeVehicle?.qtyOrders}
                    qtyDelivered={routeVehicle?.qtyDelivered}
                    qtyToDeliver={routeVehicle?.qtyToDeliver}
                  />
                </div>
              </>
            ) : (
              <>
                <div className="tracking__body__vehicles__fixed">
                  <div className="tracking__body__vehicles__fixed__top">
                    <Typography variant="h6">Vehículos</Typography>
                    <IconButton size="small" onClick={onClickReload}>
                      <RefreshIcon />
                    </IconButton>
                  </div>
                  <div className="tracking__body__vehicles__fixed__search">
                    <FormControlLabel
                      className="input warehouse"
                      control={
                        <Checkbox
                          checked={showWarehouse}
                          onChange={handleChangeWarehouse}
                          name="checked"
                          color="primary"
                        />
                      }
                      label="Mostrar almacén"
                    />
                  </div>
                  <div className="tracking__body__vehicles__fixed__search">
                    <FormControlLabel
                      className="input blocked"
                      control={
                        <Checkbox
                          checked={showBlocked}
                          onChange={handleChangeBlocked}
                          name="checked"
                          color="primary"
                        />
                      }
                      label="Mostrar bloqueos"
                    />
                  </div>
                  <Typography variant="body2" className="cant-rutas">{`${routes.length} ruta${
                    routes.length !== 1 ? 's' : ''
                  } activa${routes.length !== 1 ? 's' : ''}`}</Typography>
                  <Divider />
                </div>
                <div className="tracking__body__vehicles__list">
                  {routes &&
                    routes.map(item => (
                      <VehicleCard
                        key={item.id}
                        licensePlate={item.employeeVehicle?.vehicle.licensePlate}
                        driver={`${item.employeeVehicle?.employee.firstName} ${item.employeeVehicle?.employee.firstLastName}`}
                        vehicleType={item.employeeVehicle?.vehicle.type.id.toString()}
                        onClick={() => onClick(item.id.toString())}
                      />
                    ))}
                </div>
              </>
            )}
          </Paper>
          <TransformWrapper velocityAnimation={{ disabled: false }}>
            <TransformComponent>
              <div className="tracking__body__map">
                <div className="tracking__body__map__container">
                  <div className="tracking__body__map__container__grid">
                    <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
                      <defs>
                        <pattern
                          id="smallGrid"
                          width="16"
                          height="16"
                          patternUnits="userSpaceOnUse"
                        >
                          <path
                            d="M 16 0 L 0 0 0 16"
                            fill="none"
                            stroke="#f7f1ba"
                            strokeWidth="4"
                          />
                        </pattern>
                        <pattern id="grid" width="160" height="160" patternUnits="userSpaceOnUse">
                          <rect width="160" height="160" fill="url(#smallGrid)" />
                          <path d="M 160 0 L 0 0 0 160" fill="none" stroke="gray" strokeWidth="4" />
                        </pattern>
                      </defs>
                      <rect width="100%" height="100%" fill="url(#grid)" />
                      {blocked &&
                        showBlocked &&
                        blocked.map(item => (
                          <g>
                            <circle
                              cx={item.xCoord * distanceNode + 1}
                              cy={(ySize - item.yCoord) * distanceNode + 1}
                              r="8"
                              stroke="#222b36"
                              fill="#F44336"
                            />
                            <text
                              x={item.xCoord * distanceNode - 1.5}
                              y={(ySize - item.yCoord) * distanceNode + 4.3}
                              fontSize="10"
                            >
                              B
                            </text>
                          </g>
                        ))}
                      {isSelected ? (
                        <>
                          {routeVehicle?.nodes.map((item, index, array) =>
                            index < array.length - 1 ? (
                              <>
                                <rect
                                  x={item.xCoord * distanceNode - 1}
                                  y={(ySize - item.yCoord) * distanceNode - 1}
                                  width="4"
                                  height="4"
                                  fill="#d06aff"
                                />
                                {item.xCoord === array[index + 1].xCoord ? (
                                  <line
                                    x1={item.xCoord * distanceNode + 1}
                                    y1={(ySize - item.yCoord) * distanceNode}
                                    x2={array[index + 1].xCoord * distanceNode + 1}
                                    y2={(ySize - array[index + 1].yCoord) * distanceNode}
                                    stroke="#d06aff"
                                    strokeWidth="4"
                                  />
                                ) : (
                                  <line
                                    x1={item.xCoord * distanceNode}
                                    y1={(ySize - item.yCoord) * distanceNode + 1}
                                    x2={array[index + 1].xCoord * distanceNode}
                                    y2={(ySize - array[index + 1].yCoord) * distanceNode + 1}
                                    stroke="#d06aff"
                                    strokeWidth="4"
                                  />
                                )}
                              </>
                            ) : (
                              <></>
                            )
                          )}
                          {routeVehicle?.ordersAdress.map(item => (
                            <>
                              <circle
                                key={item.id}
                                cx={item.xCoord * distanceNode + 1}
                                cy={(ySize - item.yCoord) * distanceNode + 1}
                                r="8"
                                stroke="#222b36"
                                fill={
                                  item.state === 1
                                    ? '#4CAF50'
                                    : item.state === 10
                                    ? '#5A61A3'
                                    : '#2196f3'
                                }
                              />
                              <text
                                x={item.xCoord * distanceNode - 1.5}
                                y={(ySize - item.yCoord) * distanceNode + 4.3}
                                fontSize="10"
                              >
                                {item.state === 10
                                  ? 'A'
                                  : routeVehicle?.ordersAdress.find(node => node.state === 10)
                                  ? item.number
                                  : item.number + 1}
                              </text>
                            </>
                          ))}
                        </>
                      ) : (
                        <></>
                      )}
                    </svg>
                  </div>
                  <div className="tracking__body__map__container__elements">
                    <>
                      {vehiclePositions &&
                        vehiclePositions.map(item => (
                          <Tooltip
                            key={item.id}
                            title={`${item.type === 1 ? 'Automóvil' : 'Motocicleta'}: X=${
                              item.xPos % 1 === 0 ? item.xPos : item.xPos.toFixed(1)
                            } Y=${item.yPos % 1 === 0 ? item.yPos : item.yPos.toFixed(1)}`}
                            placement="top"
                            arrow
                          >
                            <Chip
                              icon={
                                item.type === 1 ? (
                                  <DriveEtaIcon fontSize="small" />
                                ) : (
                                  <TwoWheelerIcon fontSize="small" />
                                )
                              }
                              label={item.licensePlate}
                              color="primary"
                              className="vehicle"
                              clickable
                              size="small"
                              onClick={() => onClick(item.id.toString())}
                              onDelete={() => onClick(item.id.toString())}
                              style={{
                                width: '106px',
                                position: 'absolute',
                                left: item.xPos * distanceNode - 52,
                                bottom: item.yPos * distanceNode + 6,
                              }}
                              deleteIcon={
                                <FiberManualRecordIcon
                                  fontSize="small"
                                  className={`${
                                    item.state === 1 ? 'blue' : item.state === 0 ? 'yellow' : 'red'
                                  }`}
                                />
                              }
                            />
                          </Tooltip>
                        ))}
                      {showWarehouse && (
                        <Tooltip
                          title={`Posición: X=${warehouse?.x} Y=${warehouse?.y}`}
                          placement="top"
                          arrow
                        >
                          <Chip
                            color="primary"
                            label="Almacén"
                            icon={<StoreMallDirectoryIcon />}
                            className="vehicle"
                            size="small"
                            style={{
                              width: '86px',
                              position: 'absolute',
                              left: warehouse?.x * distanceNode - 42,
                              bottom: warehouse?.y * distanceNode + 6,
                            }}
                          />
                        </Tooltip>
                      )}
                    </>
                  </div>
                </div>
                <div className="tracking__body__map__legend">
                  <svg width="100%" height="50px" xmlns="http://www.w3.org/2000/svg">
                    <g fill="none" stroke="black" strokeWidth="3">
                      <line x1="0" y1="5" x2="0" y2="15" />
                      <line x1="161" y1="5" x2="161" y2="15" />
                      <line x1="321" y1="5" x2="321" y2="15" />
                      <line x1="481" y1="5" x2="481" y2="15" />
                      <line x1="641" y1="5" x2="641" y2="15" />
                      <line x1="801" y1="5" x2="801" y2="15" />
                      <line x1="961" y1="5" x2="961" y2="15" />
                      <line x1="1121" y1="5" x2="1121" y2="15" />
                    </g>
                    <g fontSize="14" fontFamily="Montserrat">
                      <text x="0" y="30">
                        (0,0)
                      </text>
                      <text x="140" y="30">
                        (10,0)
                      </text>
                      <text x="300" y="30">
                        (20,0)
                      </text>
                      <text x="460" y="30">
                        (30,0)
                      </text>
                      <text x="620" y="30">
                        (40,0)
                      </text>
                      <text x="780" y="30">
                        (50,0)
                      </text>
                      <text x="940" y="30">
                        (60,0)
                      </text>
                      <text x="1081" y="30">
                        (70,0)
                      </text>
                    </g>
                  </svg>
                </div>
              </div>
            </TransformComponent>
          </TransformWrapper>
        </div>
      </MainContent>
    </MainContainer>
  );
};

export default Tracking;
