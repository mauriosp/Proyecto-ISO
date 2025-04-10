import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";


const firebaseConfig = {
    apiKey: "AIzaSyCoxoJoUCBbPJZQySB9JFNecSYGKUV_BXA",
    authDomain: "santorini-hills.firebaseapp.com",
    projectId: "santorini-hills",
    storageBucket: "santorini-hills.firebasestorage.app",
    messagingSenderId: "174455149862",
    appId: "1:174455149862:web:dc7f79bb6e4a3242f21de7",
    measurementId: "G-T6MYFX6PKL"
  };

// Inicializa Firebase
const app = initializeApp(firebaseConfig);

// Exporta el storage
export const storage = getStorage(app);
