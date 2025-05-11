import React from 'react';
import { Phone, Mail, Instagram, MapPin, Clock } from 'lucide-react';

const Contato: React.FC = () => {
  return (
    <div style={styles.pageContainer}>
      <h1 style={styles.heading1}>Contato</h1>
      <div style={styles.contactContainer}>
        <div style={styles.contactSection}>
          <div style={styles.flexRow}>
            <Phone style={styles.icon} />
            <h2 style={styles.heading2}>Ligue para nós</h2>
          </div>
          <div style={styles.flexRow}>
            <a
              href="https://wa.me/83993833950"
              target="_blank"
              rel="noopener noreferrer"
              style={styles.link}
            >
              <b>(83) 9 9383-3950</b>
            </a>
          </div>
        </div>
        <div style={styles.contactSection}>
          <div style={styles.flexRow}>
            <Mail style={styles.icon} />
            <h2 style={styles.heading2}>Email</h2>
          </div>
          <div style={styles.flexRow}>
            <a href="#" target="_blank" rel="noopener noreferrer" style={styles.link}>
              <b>xxxxxx@gmail.com</b>
            </a>
          </div>
        </div>
        <div style={styles.contactSection}>
          <div style={styles.flexRow}>
            <Instagram style={styles.icon} />
            <h2 style={styles.heading2}>Instagram</h2>
          </div>
          <div style={styles.flexRow}>
            <a
              href="https://instagram.com/apaeesperanca_"
              target="_blank"
              rel="noopener noreferrer"
              style={styles.link}
            >
              <b>@apaeesperanca_</b>
            </a>
          </div>
        </div>
      </div>


      <div style={styles.centeredSection}>
        <div style={styles.flexRow}>
          <MapPin style={styles.icon} />
          <h2 style={styles.heading2}>Localização</h2>
        </div>
        <div style={styles.flexRow}>
          <a
            href="https://maps.app.goo.gl/5JwRebJbASuFdqo6A"
            target="_blank"
            rel="noopener noreferrer"
            style={styles.link}
          >
            <b>
              Rua Santo Antonio, 491<br />
              Centro, Esperança, Paraíba, Brasil
            </b>
          </a>
        </div>
      </div>

      <div style={{ marginTop: '20px' }}>
        <iframe
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3959.9499867568106!2d-35.854050699999995!3d-7.015164699999999!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x7ac28ebe0e0bd03%3A0x1a6bde6d87bda76a!2sR.%20Santo%20Ant%C3%B4nio%2C%20491%2C%20Esperan%C3%A7a%20-%20PB%2C%2058135-000!5e0!3m2!1spt-BR!2sbr!4v1746981774291!5m2!1spt-BR!2sbr"
          width="700"
          height="300"
          style={{ border: 0 }}
          allowFullScreen
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
        ></iframe>
      </div>

      <div style={{ ...styles.centeredSection, marginTop: '40px' }}>
        <div style={styles.flexRow}>
          <Clock style={styles.icon} />
          <h2 style={styles.heading2}>Horário de atendimento</h2>
        </div>
        <div style={styles.flexRow}>
          <b>Segunda à sexta, das 07:00h às 17:00h</b>
        </div>
      </div>
    </div>
  );
};

const styles = {
  pageContainer: {
    backgroundColor: '#ffffff',
    color: '#000000',
    padding: '10px',
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center', 
    alignItems: 'center', 
    textAlign: 'center' as const,
  },
  heading1: {
    fontSize: '60px',
    color: '#F28C38',
    marginBottom: '40px',
  },
  contactContainer: {
    display: 'flex',
    justifyContent: 'center',
    gap: '20px', 
    flexWrap: 'wrap', 
    marginBottom: '40px',
    width: '100%',
    maxWidth: '1200px', 
  },
  contactSection: {
    flex: '1 1 250px', 
    maxWidth: '300px',
    padding: '10px',
    textAlign: 'left' as const,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center', 
    justifyContent: 'center',
  },
  heading2: {
    fontSize: '18px',
    margin: '0',
  },
  link: {
    color: '#000000',
    textDecoration: 'none',
    fontWeight: 'bold' as const,
    fontSize: '14px',
  },
  flexRow: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    marginBottom: '10px',
  },
  icon: {
    color: '#F28C38',
    width: '40px',
    height: '40px',
  },
  centeredSection: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center', 
    justifyContent: 'center', 
    textAlign: 'center' as const,
    marginBottom: '20px',
    width: '100%',
    maxWidth: '1200px', 
  },
};

export default Contato;

