import url from  'src/utils/constant';
import * as moment from 'moment';
import axios from 'axios';
const sendBlock= ()=>{
    console.log('mostrando bloqueos')

    //var dd=new Date();
    //var date=dd.getFullYear() +' ' + dd.getMonth()
    const semana=7;

    const fechaPedido=moment.default().format('YYYY/MM/DD HH:mm');
    
    const fechaFin=moment.default().add(semana,"days").format('YYYY/MM/DD HH:mm');
    
    console.log(fechaPedido);
    console.log(fechaFin);

    const interval = setInterval(() => {
    
      const data = {
            fechaInicio: fechaPedido,
            fechaFin: fechaFin
          }
          axios.post(url + '/bloqueo/getBloqueosFechas',JSON.stringify(data) , {
              headers: { 'Content-Type': 'application/json' }
          })
          
          .then((r) => {
            console.log("entra al axios de bloqueos")
            //setBloqueos(r.data);
            console.log(r);
            console.log(data);
            console.log("Bloqueos agregado exitosamente.");         
          
          });
          
    

     }, 20000);
     return () => clearInterval(interval);
    }

export default sendBlock;