import { Route, Routes } from "react-router";
import Home from "../pages/Home";
import PostAdvertisement from "../pages/PostAdvertisement";
import Layout from "../layouts/Layout";
import SettingsPage from "../pages/SettingsPage";
import AdvertisementPageContainer from "../components/AdvertisementPageContainer";

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="advertisements/:id" element={<AdvertisementPageContainer />} />
        <Route path="settings/:setting" element={<SettingsPage />} />
      </Route>

      <Route element={<Layout />}>
        <Route path="publish" element={<PostAdvertisement />} />
        <Route path="edit/:id" element={<PostAdvertisement />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
