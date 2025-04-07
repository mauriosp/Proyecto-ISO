import { Route, Routes } from "react-router"
import Home from "../pages/Home"
import PostAdvertisement from "../pages/PostAdvertisement"
import Layout from "../layouts/Layout"

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Home />} />
      </Route>

      <Route path="postAdvertisement" element={<PostAdvertisement />} />
    </Routes>
  )
}

export default AppRoutes