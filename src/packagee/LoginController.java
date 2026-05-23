/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

/**
 *
 * @author Sahid
 */
public class LoginController {
    public Response login(String username, String password) {

        User user = HospitalData.findUserByUsername(username);

        if (user == null) {
            return new Response(
                    StatusCode.NOT_FOUND,
                    "Usuario no encontrado"
            );
        }

        if (!user.getPassword().equals(password)) {
            return new Response(
                    StatusCode.BAD_REQUEST,
                    "Contraseña incorrecta"
            );
        }

        if (user instanceof Administrator) {

            return new Response(
                    StatusCode.OK,
                    "Login administrador exitoso",
                    "ADMIN"
            );
        }

        if (user instanceof Patient) {

            return new Response(
                    StatusCode.OK,
                    "Login paciente exitoso",
                    "PATIENT"
            );
        }

        if (user instanceof Doctor) {

            return new Response(
                    StatusCode.OK,
                    "Login doctor exitoso",
                    "DOCTOR"
            );
        }

        return new Response(
                StatusCode.BAD_REQUEST,
                "Tipo de usuario inválido"
        );
    }
}
