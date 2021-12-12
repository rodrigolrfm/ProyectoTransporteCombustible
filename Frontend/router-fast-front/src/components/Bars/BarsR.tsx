import * as React from "react";
import Box from "@mui/material/Box";
import LinearProgress from "@mui/material/LinearProgress";
import { Card, CardContent, Typography } from "@mui/material";
//import { tiempo, prueba } from "src/content/applications/Transactions/PageHeader";
//import { tiempo } from "../Tiempo/tiempo";



const BarsR = () => {
  const [progress, setProgress] = React.useState(0);
  let tiempoSimulacion = 900; //simulación para 15 minutos
  //let tiempoSimulacion = (-30)*prueba + 18000;

  console.log(tiempoSimulacion);
  let intervalito = tiempoSimulacion * 10;

  React.useEffect(() => {
    const timer = setInterval(() => {
      setProgress((oldProgress) => {
        return Math.min(oldProgress + 1, 100);
      });
    }, intervalito);

    return () => {
      clearInterval(timer);
    };
  }, []);

  return (
    <Card sx={{ mt: 2 }}>
        <CardContent> 
            <Typography variant="h4">
                Progreso de la simulación:
            </Typography>
            <Box sx={{ width: "100%" }}>
        <LinearProgress
            color="primary"
            variant="determinate"
            value={progress}
        />
        </Box>
       </CardContent>
    </Card>
    
  );
}
export default BarsR;