import SignUp from './component/SignUp.jsx';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './component/Home.jsx';
import Checkout from "./component/CheckOut.jsx";
import Products from './component/Products.jsx';
import SuccessfullySignedUp from "./component/SuccessFullySignedUp.jsx";
import SuccessfullyOrdered from "./component/SuccessfullyOrdered.jsx";
import SignIn from "./component/SignIn.jsx";



function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/signup" element={<SignUp/>}/>
                    <Route path="/checkout" element={<Checkout/>}/>
                    <Route path="/products" element={<Products/>}/>
                    <Route path="/successfullySignedUp" element={<SuccessfullySignedUp/>}/>
                    <Route path="/successfullyOrdered" element={<SuccessfullyOrdered/>}/>
                    <Route path="/signin" element={<SignIn/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App
