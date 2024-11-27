'use client';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function SignIn() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const router = useRouter();

  useEffect(() => {
    if (localStorage.getItem('signInSuccess')) {
      toast.success('Sign In Successfully');
      localStorage.removeItem('signInSuccess');
    }
  }, []);

  const validateForm = () => {
    let isValid = true;
    
    setEmailError('');
    setPasswordError('');

    if (!email) {
      setEmailError('Email is required.');
      isValid = false;
    } else {
      const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
      if (!emailRegex.test(email)) {
        setEmailError('Please enter a valid email address.');
        isValid = false;
      }
    }

    if (!password) {
      setPasswordError('Password is required.');
      isValid = false;
    } else if (password.length < 8) {
      setPasswordError('Password must be at least 8 characters long.');
      isValid = false;
    }

    return isValid;
  };

  const handleSubmit = () => {
    if (!validateForm()) {
      return;
    }

    const user = JSON.parse(localStorage.getItem('user'));

    if (!user) {
      alert('No user found, please sign up first.');
      return;
    }

    if (user.email === email && user.password === password) {
      toast.success('Signed in successfully!');
      localStorage.setItem('signInSuccess', true);
      router.push('/dashboard');
    } else {
      alert('Incorrect email or password!');
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-white">
      <div className="bg-white p-8 rounded-lg w-full max-w-3xl">
        <h1 className="text-3xl font-semibold text-center mb-10">Sign In</h1>
        <form onSubmit={(e) => e.preventDefault()}>
          <div className="mb-4">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <input
              type="email"
              id="email"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            {/* Display email error */}
            {emailError && <p className="text-red-500 text-sm">{emailError}</p>}
          </div>
          <div className="mb-6">
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
              Password
            </label>
            <input
              type="password"
              id="password"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            {/* Display password error */}
            {passwordError && <p className="text-red-500 text-sm">{passwordError}</p>}
          </div>
          <button
            type="button"
            onClick={handleSubmit}
            className="w-full bg-black text-white p-3 rounded-lg hover:bg-gray-800 transition duration-300"
          >
            Sign In
          </button>
        </form>

        <p className="text-center text-sm text-blue-500 mt-4">
          <a href="/forgot-password" className="text-blue-500 hover:underline">
            Forgot Password?
          </a>
        </p>

        <p className="text-center text-sm text-blue-500 mt-4">
          <a href="/signup" className="text-blue-500 hover:underline">
            Don't have an account? Sign Up
          </a>
        </p>
      </div>

      <ToastContainer />
    </div>
  );
}
