package com.example.jpa.service;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ServiceGestion {

    private EntityManagerFactory emf;
    private EntityManager em;
    private SalleDao salleDao;
    private UtilisateurDao utilisateurDao;

    public ServiceGestion() {
        emf = Persistence.createEntityManagerFactory("gestionPU");
        em = emf.createEntityManager();

        salleDao = new SalleDao(em);
        utilisateurDao = new UtilisateurDao(em);
    }

    public void fermer() {
        if (em != null && em.isOpen()) {
            em.close();
        }

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public void ajouterSalle(Salle salle) {
        salleDao.create(salle);
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDao.create(utilisateur);
    }

    // Rechercher les salles ayant une capacité supérieure à 20
    public List<Salle> rechercherGrandesSalles() {
        return em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite > :minCapacite",
                        Salle.class
                )
                .setParameter("minCapacite", 20)
                .getResultList();
    }

    // Rechercher les utilisateurs dont le nom contient un texte
    public List<Utilisateur> rechercherUtilisateursParNom(String nom) {
        return em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern",
                        Utilisateur.class
                )
                .setParameter("pattern", "%" + nom + "%")
                .getResultList();
    }

    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDao.findAll();
    }

    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        return utilisateurDao.findBySalleNom(nomSalle);
    }
}