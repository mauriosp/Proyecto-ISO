export interface User {
    name: string;
    email: string;
    password: string;
    phone: string;
    profile?: "owner" | "renter";
}