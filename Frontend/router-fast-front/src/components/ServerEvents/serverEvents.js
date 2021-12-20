import url from  'src/utils/constant';
let datax;
//const hojasR=[];


const simulacionDia  = (funcionRequest) => {
    console.log("entrando al server event")
    
    var source = new EventSource(url +`/ejecutar/obtenerRutas`);

    source.onopen = (event) => {
        console.log ("La conexión entre el cliente y el servidor es exitosa ......");
    }
        
    source.addEventListener("RUTAS", (event) => {
        //console.log("xd");
       // startSimulation=true;
        datax=event.data;
       // hojasR.push(datax);
        //console.log(x);
        console.log("even listener");
        funcionRequest(datax);
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
    

    source.addEventListener("Routes_end", function(e) {
        console.log ("Fin de ejecución...");
        //console.log(hojasR);
        source.close();
        source = null;
    });
    source.addEventListener("error", (e) => {
        console.log ("Error de ejecución ...");
    });
    
}
export default simulacionDia;