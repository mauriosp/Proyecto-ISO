import { Route, Routes } from "react-router"
import Home from "../pages/Home"
import PostAdvertisement from "../pages/PostAdvertisement"
import Layout from "../layouts/Layout"
import AdvertisementPage from "../pages/AdvertisementPage"

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="advertisements/:id" element={<AdvertisementPage />} />
      </Route>

      <Route path="postAdvertisement" element={<PostAdvertisement />} />
    </Routes>
  )
}

export default AppRoutes