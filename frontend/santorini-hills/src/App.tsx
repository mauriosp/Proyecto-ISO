import { ModalProvider } from "./context/modal/ModalProvider";
import Home from "./pages/Home";
// import PostAdvertisement from "./pages/PostAdvertisement";

function App() {
  return (
    <ModalProvider>
      <Home/> 
      {/* <PostAdvertisement /> */}
    </ModalProvider>
  );
}

export default App;
