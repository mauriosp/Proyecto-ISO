import { useForm } from "react-hook-form";
import { FormInput } from "./FormInput";
import { useUserContext } from "../context/user/UserContext";
import { useEffect } from "react";
import { countriesList } from "../assets/countries";
import { useModalContext } from "../context/modal/ModalContext";

type EditProfileForm = {
  name: string;
  surname: string;
  phone: string;
  dialCode: string;
};

const EditForm = () => {
  const { user, setUser } = useUserContext();
  const { closeModal } = useModalContext();
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
          // First two words as name, rest as surname
          name = nameArray.slice(0, 2).join(" ");
          surname = nameArray.slice(2).join(" ");
        } else {
          // First word as name, second as surname
          name = nameArray[0] || "";
          surname = nameArray[1] || "";
        }

        return { name, surname };
      };

      const { name, surname } = getName();
      getName();
      const dialCode = user.phone.slice(0, 3);
      const phone = user.phone.slice(3);
      reset({
        name: name || "",
        surname: surname || "",
        dialCode: dialCode || "+57",
        phone: phone || "",
      });
    }
  }, [user, reset]);

  const onSubmit = (data: EditProfileForm) => {
    if (!user?.email) return;

    const updatedUser = {
      ...user,
      name: `${data.name} ${data.surname}`,
      phone: `${data.dialCode}${data.phone}`,
      email: user.email,
    };

    setUser(updatedUser);
    closeModal();
  };

  return (
    <form className="flex flex-col space-y-2" onSubmit={handleSubmit(onSubmit)}>
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

      <button
        className="form-button mt-4 hover:bg-slate-800 bg-accent text-white font-semibold"
        type="submit"
      >
        Guardar cambios
      </button>
    </form>
  );
};

export default EditForm;
