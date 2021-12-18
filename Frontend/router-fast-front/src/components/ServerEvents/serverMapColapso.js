import url from  'src/utils/constant';



let datax;
const fecha={
   fecha: "2021-12-18 00:00:00"

}
const simulacionColapso  = (funcionRequest) => {
    

    var source = new EventSource(url +`/ejecutar/simularRutasColapso?fecha=2021-12-18%2000:00:00`, {
        headers: { 'Content-Type': 'application/json' }
    });
    //var source = new EventSource(url +`/ejecutar/obtenerTresDias`);

    source.onopen = (event) => {
        console.log ("La conexión entre el cliente y el servidor es exitosa ......");
    }
    
    
    source.addEventListener("RUTAS", (event) => {
        console.log("server event para colapso logistico");
        datax=event.data;
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
    /*

    source.addEventListener("Routes_end", function(e) {
        console.log ("Fin de ejecución...");
        source.close();
        source = null;
        funcionEnd();
    });
    */
}
export default simulacionColapso;