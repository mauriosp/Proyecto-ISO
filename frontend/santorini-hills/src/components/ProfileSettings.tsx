import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { FormInput } from "./FormInput";
import { useUserContext } from "../context/user/UserContext";
import { useModalContext } from "../context/modal/ModalContext";
import { countriesList } from "../assets/countries";
import { eliminarCuenta } from "../utils/APICalls";
import { actualizarPerfil } from "../utils/APICalls";
import { uploadFileToFirebase } from "../utils/UploadFileToFirebase";

type EditProfileForm = {
  name: string;
  surname: string;
  phone: string;
  dialCode: string;
  profilePhoto: FileList;
};

const ProfileSettings = () => {
  const { user, setUser } = useUserContext();
  const { closeModal } = useModalContext();

  const storedUser = localStorage.getItem("loggedUser");
  const idUsuario = storedUser ? JSON.parse(storedUser).id : "";

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<EditProfileForm>();

  useEffect(() => {
    if (user) {
      const getName = () => {
        const nameArray = user.name.split(" ");
        let name, surname;
        if (nameArray.length > 2) {
          name = nameArray.slice(0, 2).join(" ");
          surname = nameArray.slice(2).join(" ");
        } else {
          name = nameArray[0] || "";
          surname = nameArray[1] || "";
        }
        return { name, surname };
      };

      const { name, surname } = getName();
      const dialCode = user.phone.slice(0, 3);
      const phone = user.phone.slice(3);
      reset({
        name: name || "",
        surname: surname || "",
        dialCode: dialCode || "+57",
        phone: phone || "",
        profilePhoto: undefined,
      });
    }
  }, [user, reset]);

  const onSubmit = async (data: EditProfileForm) => {
    if (!user?.email) return;

    let photoURL = user.photo;

    // Subir nueva imagen si se seleccionó
    if (data.profilePhoto && data.profilePhoto.length > 0) {
      const file = data.profilePhoto[0];
      photoURL = await uploadFileToFirebase(file); // Subir a Firebase y obtener URL
    }

    const nombreCompleto = `${data.name} ${data.surname}`;
    const telefonoCompleto = data.phone;

    try {
      await actualizarPerfil({
        id: idUsuario,
        nombre: nombreCompleto,
        telefono: telefonoCompleto,
        fotoPerfil: photoURL, // URL como string
      });

      const updatedUser = {
        ...user,
        name: nombreCompleto,
        phone: telefonoCompleto,
        email: user.email,
        photo: photoURL,
      };

      setUser(updatedUser);
      closeModal();

      // Opcional: feedback visual
      setTimeout(() => {
        alert("Perfil actualizado correctamente");
      }, 300);
    } catch (error) {
      console.error("Error al actualizar el perfil:", error);
      alert("Hubo un problema al actualizar el perfil.");
    }
  };

  const handleDeleteAccount = () => {
    const confirmed = window.confirm("¿Estás seguro que deseas eliminar tu cuenta?");
    if (confirmed) {
      setUser(null);
      localStorage.removeItem("loggedUser");
      // Llamado a la API para eliminar la cuenta
      eliminarCuenta(idUsuario);

      window.location.href = "/";
    }
  };

  return (
    <form className="flex flex-col space-y-2 w-full" onSubmit={handleSubmit(onSubmit)}>
      {/* Espacio para foto de perfil y subir nueva imagen */}
      <div className="flex flex-col items-center">
        {user && user.photo ? (
          <img
            src={user.photo}
            alt="Foto de perfil"
            className="w-20 aspect-square rounded-full object-cover mb-2"
          />
        ) : (
          <div className="w-20 aspect-square rounded-full bg-gray-200 flex items-center justify-center mb-2">
            Sin foto
          </div>
        )}
        <label className="font-medium cursor-pointer text-accent" htmlFor="profile-photo">
          Subir foto de perfil
        </label>
        <input
          id="profile-photo"
          type="file"
          accept="image/jpeg, image/jpg, image/png"
          {...register("profilePhoto", {
            validate: {
              fileType: (value: FileList) => {
                if (value.length === 0) return true;
                const file = value[0];
                const allowedTypes = ["image/jpeg", "image/jpg", "image/png"];
                return (
                  allowedTypes.includes(file.type) ||
                  "Formato inválido. Usa jpeg, jpg o png."
                );
              },
              fileSize: (value: FileList) => {
                if (value.length === 0) return true;
                const file = value[0];
                return (
                  file.size <= 5 * 1024 * 1024 ||
                  "El archivo debe ser menor a 5MB."
                );
              },
            },
          })}
        />
        {errors.profilePhoto && (
          <p className="text-red-500 text-sm">{errors.profilePhoto.message}</p>
        )}
      </div>

      {/* Nombre */}
      <div>
        <label className="form-label" htmlFor="name-input">
          Nombre
        </label>
        <FormInput
          id="name-input"
          placeholder="Escribe tu nombre"
          {...register("name", {
            required: "El nombre es obligatorio",
            pattern: {
              value: /^[a-zA-ZÀ-ÿ\s]+$/,
              message: "El nombre solo debe contener letras",
            },
          })}
        />
        {errors.name && (
          <p className="text-red-500 text-sm">{errors.name.message}</p>
        )}
      </div>

      {/* Apellido */}
      <div>
        <label className="form-label" htmlFor="surname-input">
          Apellido
        </label>
        <FormInput
          id="surname-input"
          placeholder="Escribe tu apellido"
          {...register("surname", {
            required: "El apellido es obligatorio",
            pattern: {
              value: /^[a-zA-ZÀ-ÿ\s]+$/,
              message: "El apellido solo debe contener letras",
            },
          })}
        />
        {errors.surname && (
          <p className="text-red-500 text-sm">{errors.surname.message}</p>
        )}
      </div>

      {/* Celular */}
      <div>
        <label className="form-label" htmlFor="phone-input">
          Celular
        </label>
        <div className="flex gap-2">
          <select
            className="font-medium text-sm py-3 px-3 w-min shadow-md rounded-xl border-accent border outline-none transition-all"
            defaultValue={"+57"}
            {...register("dialCode")}
          >
            {countriesList.map((country) => (
              <option key={country.code} value={country.dial_code}>
                {country.flag}
              </option>
            ))}
          </select>
          <FormInput
            id="phone-input"
            placeholder="Ej. (123) 456 - 7890"
            type="tel"
            {...register("phone", {
              required: "El número de teléfono es obligatorio",
              pattern: {
                value: /^[0-9]{10}$/,
                message: "Debe contener 10 dígitos numéricos",
              },
            })}
          />
        </div>
        {errors.phone && (
          <p className="text-red-500 text-sm">{errors.phone.message}</p>
        )}
      </div>

      {/* Botón de guardar cambios */}
      <button
        className="form-button mt-4 hover:bg-slate-800 bg-accent text-white font-semibold"
        type="submit"
      >
        Guardar cambios
      </button>

      {/* Botón para eliminar cuenta */}
      <button
        type="button"
        onClick={handleDeleteAccount}
        className="form-button hover:bg-red-800 bg-red-600 text-white font-semibold"
      >
        Eliminar cuenta
      </button>
    </form>
  );
};

export default ProfileSettings;
