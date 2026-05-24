/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zedanc
 */
public class LoadData {
//Tiene el throws exception para q quien lo llame se encargue de manejar la excepción
    public static void loadUsers () throws Exception  {
        //Leer y crear el objeto
        String content = new String(
                Files.readAllBytes(Paths.get("json/users.json"))
        );
        JSONObject root = new JSONObject(content);

        //Obtengo el array de users y los recorro
        JSONArray users = root.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {

            JSONObject obj = users.getJSONObject(i);
            String type = obj.getString("type");

            //Para cuando sea admin
            if (type.equals("admin")) {

                Administrator admin = new Administrator(
                        obj.getLong("id"),
                        obj.getString("username"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        obj.getString("password")
                );

                DataBase.administrators.add(admin);
            } //Para los patient
            else if (type.equals("patient")) {

                Patient patient = new Patient(
                        obj.getLong("id"),
                        obj.getString("username"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        obj.getString("password"),
                        obj.getString("email"),
                        LocalDate.parse(obj.getString("birthdate")),
                        obj.getBoolean("gender"),
                        obj.getLong("phone"),
                        obj.getString("address")
                );

                DataBase.patients.add(patient);
            } //Para el doctor
            else if (type.equals("doctor")) {

                Doctor doctor = new Doctor(
                        obj.getLong("id"),
                        obj.getString("username"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        obj.getString("password"),
                        Specialty.valueOf(obj.getString("specialty")),
                        obj.getString("licenceNumber"),
                        obj.getString("assignedOffice")
                );

                DataBase.doctors.add(doctor);
            }

        }

    }

}
