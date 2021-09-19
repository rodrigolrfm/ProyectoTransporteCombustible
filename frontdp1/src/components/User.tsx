import *as React from 'react';
import { Alert, Button, Card, CardBody, CardImg, CardText, CardTitle, Col, Container, Form, Media, Row } from 'reactstrap';
import './User.css';
import logo from '../assets/imgs/logorouterfas.png';
import logos from './logo.svg';


class User extends React.Component<any, any>{

    handleNewUser() {

    }

    render() {
        return (
            <div className="limiter">
		<div className="container-login100">
			<div className="wrap-login100 p-t-190 p-b-30">
				<form className="login100-form validate-form">
					<div className="login100-form-avatar">
						<img src="logorouterfas.png" alt="RouterFast"/>
                        <Media object data-src="src\components\logorouterfas.png" alt="xd" />
					</div>
                    

					<span className="login100-form-title p-t-20 p-b-45">
						Iniciar sesión
					</span>

					<div className="wrap-input100 validate-input m-b-10" data-validate = "Username is required">
						<input className="input100" type="text" name="username" placeholder="Usuario"/>
						<span className="focus-input100"></span>
						<span className="symbol-input100">
							<i className="fa fa-user"></i>
						</span>
					</div>                    
					<div className="wrap-input100 validate-input m-b-10" data-validate = "Password is required">
						<input className="input100" type="password" name="pass" placeholder="Contraseña"/>
						<span className="focus-input100"></span>
						<span className="symbol-input100">
							<i className="fa fa-lock"></i>
						</span>
					</div>

					<div className="container-login100-form-btn p-t-10">
						<button className="login100-form-btn">
							Login
						</button>
					</div>

					<div className="text-center w-full p-t-25 p-b-230">
						<a href="#" className="txt1">
							Forgot Username / Password?
						</a>
					</div>

					<div className="text-center w-full">
						<a className="txt1" href="#">
							Create new account
							<i className="fa fa-long-arrow-right"></i>						
						</a>
					</div>
				</form>
			</div>
		</div>
        <Alert color="primary">
     This is a primary alert — check it out!
   </Alert>

   <div>
      <Button color="secondary">primary</Button>{' '}
      <Button color="secondary">secondary</Button>{' '}
      <Button color="success">success</Button>{' '}
      <Button color="info">info</Button>{' '}
      <Button color="warning">warning</Button>{' '}
      <Button color="danger">danger</Button>{' '}
      <Button color="link">link</Button>
    </div>
	</div>
    
            
        )
    }
}
export default User;