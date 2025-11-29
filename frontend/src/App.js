import logo from './logo.svg';
import './App.css';
import {Route, Router, Routes} from "react-router-dom";
import Home from "./components/Home";
import Generate from "./components/Generate";
import Flashcards from "./components/Flashcard"
function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/generate" element={<Generate />} />
          <Route path="/flashcards" element={<Flashcards />} />
        </Routes>
      </Router>
  );
}

export default App;
