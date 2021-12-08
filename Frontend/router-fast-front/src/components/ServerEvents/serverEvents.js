import url from  'src/utils/constant';

const simulacionDia  = (funcionRequest) => {
    var source = new EventSource(url +`/ejecutar/obtenerRutas`);

    source.onopen = function (event) {
        console.log ("La conexión entre el cliente y el servidor es exitosa ......");
    }
    
    
    source.addEventListener("v_requests", function(event) {
        console.log("xd");
        let data = JSON.parse(event.data);
        funcionRequest(event.data);
    })


   
    /* 
    source.addEventListener("v_blocks", function(event) {
        let data = JSON.parse(event.data);
        funcionBlock(event.data);
    })
    
    source.addEventListener("error", function(e) {
        console.log ("Error de ejecución ...");
        source.close();
        source = null;
        funcionError();
    });
    

    source.addEventListener("Routes_end", function(e) {
        console.log ("Fin de ejecución...");
        source.close();
        source = null;
        funcionEnd();
    });
    */
}
export default simulacionDia;