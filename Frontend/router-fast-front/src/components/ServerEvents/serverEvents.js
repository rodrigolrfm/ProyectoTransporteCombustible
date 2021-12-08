import url from  'src/utils/constant';

const simulacionDia  = (funcionRequest) => {
    var source = new EventSource(url +`/ejecutar/obtenerRutas`);

    source.onopen = (event) => {
        console.log ("La conexión entre el cliente y el servidor es exitosa ......");
    }
    
    
    source.addEventListener("RUTAS", (event) => {
        console.log("xd");
        console.log(event.data);
        //funcionRequest(event.data);
    });

    /* 
    source.addEventListener("v_blocks", function(event) {
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
export default simulacionDia;