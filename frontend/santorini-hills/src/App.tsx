// import Login from "./pages/Login";
import { useModalContext } from "./context/modal/ModalContext";
// import Login from "./pages/Login";
import Register from "./pages/Register";

function App() {
  const {message} = useModalContext();
  return (
    <>
        <div className="flex justify-center items-center h-screen bg-neutral-100">
          <div className="min-w-md">
            <h3 className="text-center text-xl font-semibold my-10">
              {message}
            </h3>
            {/* <Login/> */}
            <Register />
          </div>
        </div>
    </>
  );
}

export default App;
