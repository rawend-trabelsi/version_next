'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import 'bootstrap/dist/css/bootstrap.min.css'; 

export default function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [error, setError] = useState(''); 
  const [successMessage, setSuccessMessage] = useState(''); 
  const router = useRouter();

  const generateCode = () => {
    return Math.floor(100000 + Math.random() * 900000).toString(); 
  };

  const handleSendCode = () => {
    if (!email) {
      setError('Enter your email address');
      setSuccessMessage(''); 
      return;
    }

    setError('');

    const code = generateCode();
    localStorage.setItem('verificationCode', code);  
    localStorage.setItem('userEmail', email); // Store email

    setSuccessMessage(`A verification code has been sent to ${email}.`);

    // Redirect to the verification code page
    setTimeout(() => {
      router.push('/send-verification-code');
    }, 2000); 
  };

  const handleGoBack = () => {
    router.push('/signin'); 
  };

  return (
    <div className="flex items-center justify-center h-screen bg-white">
      <div className="bg-white p-8 rounded-lg w-full max-w-3xl">
        <h1 className="text-3xl font-semibold text-center mb-10">Forgot Password</h1>

        {successMessage && (
          <div className="mb-4 p-3 text-black bg-[#B8EBC8] rounded-lg text-center">
            {successMessage}
          </div>
        )}

        <form onSubmit={(e) => e.preventDefault()}>
          <div className="mb-4">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2"> 
              Email
            </label>
            <input
              type="email"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            {error && (
              <p className="text-red-500 text-sm mt-2">{error}</p>
            )}
          </div>
          <button
            type="button"
            onClick={handleSendCode}
            className="w-full bg-black text-white p-3 rounded-lg hover:bg-gray-800 transition duration-300"
          >
            Send Reset Code
          </button>
        </form>

        <div className="mt-4 text-center">
          <p className="text-sm text-gray-600">
            Remember your password?{' '}
            <a
              onClick={handleGoBack}
              className="text-blue-500 hover:underline cursor-pointer"
            >
              Sign In
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
