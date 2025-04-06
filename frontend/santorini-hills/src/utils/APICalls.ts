import axios from "axios";
import { User } from "../models/user";

export const register = async (user: User) => {
  try {
    console.log({user});
    const response = await axios.post("/auth/register", user);
    return response.data;
  } catch (error) {
    return error;
  }
};

export const login = async (email: string, password: string) => {
  try {
    const response = await axios.post("/auth/login", { email, password });
    return response.data;
  } catch (error) {
    return error;
  }
}