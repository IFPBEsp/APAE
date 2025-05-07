import React from 'react';

interface FooterProps {}

const Footer: React.FC<FooterProps> = () => {
  return (
    <footer style={styles.footer}>
      <div style={styles.section}>
        <p><b>Telefone:</b></p>
        <a href='https://wa.me/83993833950' style={{ marginTop: '5px' }}><b>(83) 9 9383-3950</b></a>
      </div>
      <div style={styles.section}>
        <p><b>Siga-nos</b></p>
        <a href='https://instagram.com/apaeesperanca_' style={{ marginTop: '5px' }}><b>@apaeesperanca_</b></a>
      </div>
      <div style={styles.section}>
        <p><b>Nossa Localização</b></p>
        <a href='https://maps.app.goo.gl/5JwRebJbASuFdqo6A' style={{ marginTop: '5px' }}>
          <b>
            Rua Santo Antonio, 491
            <br></br>
            Centro, Esperança, Paraíba, Brasil
          </b>
        </a>
      </div>
    </footer>
  );
};


const styles = {
  footer: {
    padding: '15px 500px',
    width: '100vw',
    backgroundColor: '#165BAA',
    display: 'flex',
    justifyContent: 'space-between',
    flexWrap: 'wrap' as 'wrap',
    borderTop: '1px solid #ddd',
  },
  section: {
    flex: '1 1 30%',
    padding: '10px',
    marginBottom: '20px',
  },
};

export default Footer;
