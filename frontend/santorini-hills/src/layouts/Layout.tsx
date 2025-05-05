import { Outlet } from "react-router";
import Modal from "../components/Modal";
import Navbar from "../components/Navbar"
import { useModalContext } from "../context/modal/ModalContext"
import { useUserContext } from "../context/user/UserContext";
import { useEffect } from "react";
import ChatSystem from "../components/ChatSystem";

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
        <div className="min-h-screen flex flex-col">
            <Navbar />
            <div className="flex-1 flex flex-col">
                <Outlet />
            </div>
            {isOpen && (
                <div className="flex justify-center items-center bg-neutral-100">
                    <div className="min-w-md">
                        <Modal />
                    </div>
                </div>
            )}
            <ChatSystem />
        </div>

    )
}

export default Layout