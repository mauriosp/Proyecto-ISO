import axios from "axios";
import { User } from "../models/user";
import { userAdapter } from "../presenters/presenters";

export const register = async (user: User) => {
  try {
    const userPost = userAdapter(user);
    console.log(userPost);
    const response = await axios.post("/auth/register", userPost);
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