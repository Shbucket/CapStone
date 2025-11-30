import {BrowserRouter, Route, Router, Routes} from "react-router-dom";
import Home from "./components/Home";
import Generate from "./components/Generate";
import Flashcards from "./components/FlashcardsPage"
import SignInPage from "./components/SignIn";
import SignUpPage from "./components/SIgnUp";
function App() {
  return (
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/generate" element={<Generate />} />
          <Route path="/flashcards" element={<Flashcards />} />
            <Route path="/sign-in" element={<SignInPage />} />
            <Route path="/sign-up" element={<SignUpPage />} />
            <Route
                path="/sign-in/verify-email-address"
                element={<SignInPage path="/sign-in" routing="path" />}
            />
            <Route
                path="/sign-up/verify-email-address"
                element={<SignUpPage path="/sign-up" routing="path" />}
            />
        </Routes>
  );
}

export default App;
