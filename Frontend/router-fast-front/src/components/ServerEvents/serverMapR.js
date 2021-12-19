import url from  'src/utils/constant';

let datax;
let dataBloqueos;

const fecha={
   fecha: "2021-12-18 00:00:00"

}
const simulacion3Dias  = (funcionRequest) => {
    
    var source = new EventSource(url +`/ejecutar/obtenerTresDias`, {
        headers: { 'Content-Type': 'application/json' }
    });
    //var source = new EventSource(url +`/ejecutar/obtenerTresDias`);

    source.onopen = (event) => {
        console.log ("La conexión entre el cliente y el servidor es exitosa ......");
    }    
    source.addEventListener("3dias", (event) => {
        console.log("server event para tres dias");
        datax=event.data;
        //console.log(datax);
        funcionRequest(event.data);
    });

    source.addEventListener("bloqueostres", (event) => {
        console.log("server event de bloqueos para tres dias");
        dataBloqueos=event.data;
        //console.log(datax);
        funcionRequest(event.data);
    });

    /* 
    source.addEventListener("bloqueo", function(event) {
        funcionBlock(event.data);
    })
    */
    source.addEventListener("error", (e) => {
        console.log ("Error de ejecución ...");
        source.close();
        source = null;
        //funcionError();
    });
    
    source.addEventListener("STOP", function(e) {
        console.log("finalización de 3 días");
        // AQUI IRE EL MODAL DE HOJA DE RUTAS


        source = null;

    });
    
}
export default simulacion3Dias;