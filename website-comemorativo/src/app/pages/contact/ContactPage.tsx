import React from 'react';
import { Phone, Mail, Instagram, MapPin, Clock } from 'lucide-react';
import Styles from './page.module.css';

const Contato: React.FC = () => {
  return (
    <div className={Styles.pageContainer}>
      <h1 className={Styles.heading1}>Contato</h1>
      <div className={Styles.contactContainer}>
        <div className={Styles.contactSection}>
          <div className={Styles.flexRow}>
            <Phone className={Styles.icon} />
            <h2 className={Styles.heading2}>Ligue para nós</h2>
          </div>
          <div className={Styles.flexRow}>
            <a
              href="https://wa.me/83993833950"
              target="_blank"
              rel="noopener noreferrer"
              className={Styles.link}
            >
              <b>(83) 9 9383-3950</b>
            </a>
          </div>
        </div>

        <div className={Styles.contactSection}>
          <div className={Styles.flexRow}>
            <Mail className={Styles.icon} />
            <h2 className={Styles.heading2}>Email</h2>
          </div>
          <div className={Styles.flexRow}>
            <a href="#" target="_blank" rel="noopener noreferrer" className={Styles.link}>
              <b>xxxxxx@gmail.com</b>
            </a>
          </div>
        </div>

        <div className={Styles.contactSection}>
          <div className={Styles.flexRow}>
            <Instagram className={Styles.icon} />
            <h2 className={Styles.heading2}>Instagram</h2>
          </div>
          <div className={Styles.flexRow}>
            <a
              href="https://instagram.com/apaeesperanca_"
              target="_blank"
              rel="noopener noreferrer"
              className={Styles.link}
            >
              <b>@apaeesperanca_</b>
            </a>
          </div>
        </div>
      </div>

      <div className={Styles.centeredSection}>
        <div className={Styles.flexRow}>
          <MapPin className={Styles.icon} />
          <h2 className={Styles.heading2}>Localização</h2>
        </div>
        <div className={Styles.flexRow}>
          <a
            href="https://maps.app.goo.gl/5JwRebJbASuFdqo6A"
            target="_blank"
            rel="noopener noreferrer"
            className={Styles.link}
          >
            <b>
              Rua Santo Antonio, 491<br />
              Centro, Esperança, Paraíba, Brasil
            </b>
          </a>
        </div>
      </div>

      <div className={Styles.mapContainer}>
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

      <div className={Styles.centeredSection}>
        <div className={Styles.flexRow}>
          <Clock className={Styles.icon} />
          <h2 className={Styles.heading2}>Horário de atendimento</h2>
        </div>
        <div className={Styles.flexRow}>
          <b>Segunda à sexta, das 07:00h às 17:00h</b>
        </div>
      </div>
    </div>
  );
};

export default Contato;