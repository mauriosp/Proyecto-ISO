import { Route, Routes } from "react-router";
import Home from "../pages/Home";
import PostAdvertisement from "../pages/PostAdvertisement";
import Layout from "../layouts/Layout";
import ProfilePage from "../pages/ProfilePage";
import AdvertisementPageContainer from "../components/AdvertisementPageContainer";

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="advertisements/:id" element={<AdvertisementPageContainer />} />
        <Route path="profile" element={<ProfilePage />} />
      </Route>

      <Route path="postAdvertisement" element={<PostAdvertisement />} />
    </Routes>
  );
};

export default AppRoutes;
