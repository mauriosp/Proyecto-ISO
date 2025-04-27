import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import { UserProvider } from './context/user/UserProvider.tsx'
import './index.css'
import { BrowserRouter } from 'react-router'
import { ModalProvider } from './context/modal/ModalProvider.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <ModalProvider>
      <UserProvider>
        <App />
      </UserProvider>
    </ModalProvider>
  </BrowserRouter>
)
