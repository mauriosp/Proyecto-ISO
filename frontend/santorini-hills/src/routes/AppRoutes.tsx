import { Route, Routes } from "react-router"
import Home from "../pages/Home"
import PostAdvertisement from "../pages/PostAdvertisement"

const AppRoutes = () => {
  return (
    <Routes>
        <Route index element={<Home/>}/>
        <Route path="postAdvertisement" element={<PostAdvertisement/>}/>
    </Routes>
  )
}

export default AppRoutes