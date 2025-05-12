import React from 'react';
import Styles from './Footer.module.css';

export default function Footer() {
  return (
    <footer className={Styles.footer}>
      <div className={Styles.section}>
        <p><b>Telefone:</b></p>
        <a href="https://wa.me/83993833950" className={Styles.content}>
          <b>(83) 9 9383-3950</b>
        </a>
      </div>
      <div className={Styles.section}>
        <p><b>Siga-nos</b></p>
        <a href="https://instagram.com/apaeesperanca_" className={Styles.content}>
          <b>@apaeesperanca_</b>
        </a>
      </div>
      <div className={Styles.section}>
        <p><b>Nossa Localização</b></p>
        <a href="https://maps.app.goo.gl/5JwRebJbASuFdqo6A" className={Styles.content}>
          <b>
            Rua Santo Antonio, 491
            <br />
            Centro, Esperança, Paraíba, Brasil
          </b>
        </a>
      </div>
    </footer>
  );
}