export interface EmailVerification {
    Token: string;
    fechaCreacion: string;
    fechaExpiracion: string;
    tipoVerificacion: string;
    verificado: boolean;
  }
  