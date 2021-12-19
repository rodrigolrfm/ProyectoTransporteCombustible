import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import url from  'src/utils/constant';

this.socket=new SockJS( url +'/ejecutar/obtenerRutas');
        this.stompClient=Stomp.over(this.socket);
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/topic/estado-general',(greeting)=>{
                console.log(greeting);
                let jsonGreeting=JSON.parse(greeting.body);
                console.log(jsonGreeting);
                //console.log(this.pedidosActuales);
                this.obtenerPosicionesYBloqueosActuales(jsonGreeting);
            });
        });