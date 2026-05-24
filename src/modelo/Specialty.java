/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package modelo;

/**
 *
 * @author edangulo
 */
public enum Specialty {
    
    GENERAL_MEDICINE,
    CARDIOLOGY,
    PEDIATRICS,
    NEUROLOGY,
    TRAUMATOLOGY_ORTHOPEDICS,
    GYNECOLOGY_OBSTETRICS,
    GYNECOLOGY,
    DERMATOLOGY,
    PSYCHIATRY,
    ONCOLOGY,
    ORTHOPEDICS,
    OPHTHALMOLOGY,
    INTERNAL_MEDICINE;
    
    public static Specialty fromDisplayName(String text) {
        switch (text) {
            case "General Medicine":
                return GENERAL_MEDICINE;

            case "Cardiology":
                return CARDIOLOGY;

            case "Pediatrics":
                return PEDIATRICS;

            case "Neurology":
                return NEUROLOGY;

            case "Traumatology & Orthopedics":
                return TRAUMATOLOGY_ORTHOPEDICS;

            case "Gynecology & Obstetrics":
                return GYNECOLOGY_OBSTETRICS;

            case "Dermatology":
                return DERMATOLOGY;

            case "Psychiatry":
                return PSYCHIATRY;

            case "Oncology":
                return ONCOLOGY;

            case "Ophthalmology":
                return OPHTHALMOLOGY;

            case "Internal Medicine":
                return INTERNAL_MEDICINE;

            default:
                return null;
        }
    }

}
//Este fue el enum al que le faltaban dos especialidades: GYNECOLOGY Y ORTHOPEDICS