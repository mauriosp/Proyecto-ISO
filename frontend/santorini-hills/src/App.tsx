import { ModalProvider } from "./context/modal/ModalProvider";
import Home from "./pages/Home";

function App() {
  return (
    <ModalProvider>
      <Home/>
    </ModalProvider>
  );
}

export default App;
