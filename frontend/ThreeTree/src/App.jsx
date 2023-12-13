import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './component/Home.jsx';
import SignUp from './component/SignUp.jsx';
import SignIn from './component/SignIn.jsx';
import Checkout from './component/CheckOut.jsx';
import Products from './component/Products.jsx';

function App() {
    return (
        <BrowserRouter caseSensitive>
            <div>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/signin" element={<SignIn />} />
                    <Route path="/checkout" element={<Checkout />} />
                    <Route path="/products" element={<Products />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;

