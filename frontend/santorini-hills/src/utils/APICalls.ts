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
