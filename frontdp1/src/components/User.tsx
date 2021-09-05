import *as React from 'react';
import { Button, Card, Col, Container, Form, Row } from 'reactstrap'

class User extends React.Component<any, any>{

    handleNewUser() {

    }

    render() {
        return (
            <div className="card card-body">
                <form onSubmit={e => this.handleNewUser()}>
                    <label className="form-label">Email address</label>
                    <input type="text" className="form-control" id="exampleFormControlInput1" placeholder="name@example.com" />
     
                    <button type="button" className="btn btn-primary mb-3">Primary</button>
                    
                </form>
                <div className="card">
                    <img src="https://areajugones.sport.es/wp-content/uploads/2021/02/pikachu-pokemon.jpg" className="card-img-top"></img>
                    <div className="card-body">
                        <h5 className="card-title">Card title</h5>
                        <p className="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                        <a href="#" className="btn btn-primary">Go somewhere</a>
                    </div>
                </div>
                <div className="card">
                    <img src="..." className="card-img-top" alt="..."/>
                    <div className ="card-body">
                    <h5 className ="card-title">Card title</h5>
                    <p className ="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                    <a href="#" className ="btn btn-primary">Go somewhere</a>
                    </div>
                </div>
            </div>

        )
    }
}
export default User;