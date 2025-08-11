import ButtonWrapper from './components/ButtonWrapper';
import LoginForm from './components/LoginForm';

export default function Home() {
  return (
      <div 
        className="flex items-center justify-center min-h-screen p-4 font-sans bg-cover bg-center"
        style={{ backgroundImage: 'url("/apae.svg")' }}
      >
        <LoginForm />
    </div>
  );
}