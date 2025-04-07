import { Outlet } from "react-router";
import Modal from "../components/Modal";
import Navbar from "../components/Navbar"
import { useModalContext } from "../context/modal/ModalContext"
import { useUserContext } from "../context/user/UserContext";
import { useEffect } from "react";

const Layout: React.FC<React.PropsWithChildren> = () => {
    const { isOpen, openModal } = useModalContext();
    const { user } = useUserContext();
    useEffect(() => {
        if (user && !user?.isVerified) {
            openModal("verification");
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [user]);
    return (
        <>
            <Navbar />
            <Outlet />
            {isOpen && (
                <div className="flex justify-center items-center bg-neutral-100">
                    <div className="min-w-md">
                        <Modal />
                    </div>
                </div>
            )}
        </>
    )
}

export default Layout