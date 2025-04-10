import { ref, uploadBytes, getDownloadURL } from "firebase/storage";
import { v4 as uuidv4 } from "uuid";
import { storage } from "./firebase";

export const uploadFileToFirebase = async (file: File): Promise<string> => {
  const fileRef = ref(storage, `advertisement-images/${uuidv4()}-${file.name}`);
  await uploadBytes(fileRef, file);
  const downloadURL = await getDownloadURL(fileRef);
  return downloadURL;
};
