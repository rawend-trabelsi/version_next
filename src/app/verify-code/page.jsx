'use client';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import 'bootstrap/dist/css/bootstrap.min.css';

export default function VerifyCode() {
  const [code, setCode] = useState(['', '', '', '', '', '']);
  const [correctCode, setCorrectCode] = useState('');
  const [email, setEmail] = useState('');
  const [resendAttempts, setResendAttempts] = useState(3);
  const [cooldown, setCooldown] = useState(0);
  const router = useRouter();

  useEffect(() => {
    const storedEmail = localStorage.getItem('userEmail');
    const storedCode = localStorage.getItem('verificationCode');
    const storedAttempts = parseInt(localStorage.getItem(`${storedEmail}_resendAttempts`)) || 3;
    const storedCooldown = parseInt(localStorage.getItem(`${storedEmail}_cooldown`)) || 0;

    if (storedCode && storedEmail) {
      setCorrectCode(storedCode.trim());
      setEmail(storedEmail);
      setResendAttempts(storedAttempts);

      // Calculate the remaining cooldown time
      const currentTime = Math.floor(Date.now() / 1000);
      const remainingTime = storedCooldown - currentTime;

      if (remainingTime > 0) {
        setCooldown(remainingTime);
        const timerId = setInterval(() => {
          setCooldown(prev => {
            if (prev <= 1) {
              clearInterval(timerId);
              return 0; // Reset cooldown
            }
            return prev - 1;
          });
        }, 1000);

        return () => clearInterval(timerId); // Cleanup on unmount
      }
    } else {
      router.push('/send-verification-code'); // Redirect if no email or code is found
    }
  }, [router]);

  const handleVerifyCode = () => {
    const inputCode = code.join('');
    if (inputCode === correctCode) {
      router.push('/reset-password'); // Navigate to reset password page
    } else {
      alert('Incorrect verification code. Please try again.');
    }
  };

  const handleChange = (e, index) => {
    const value = e.target.value;
    if (/^\d$/.test(value) || value === '') {
      const newCode = [...code];
      newCode[index] = value;
      setCode(newCode);
      if (value !== '' && index < 5) {
        document.getElementById(`input-${index + 1}`).focus();
      }
    }
  };

  const handleResendCode = () => {
    if (resendAttempts > 0) {
      // Generate a new verification code
      const newCode = Math.floor(100000 + Math.random() * 900000).toString();
      setCorrectCode(newCode); // Update the correct code state
      localStorage.setItem('verificationCode', newCode); // Update local storage

      alert(`A new verification code has been sent to ${email}.`);

      // Update resend attempts
      const newAttempts = resendAttempts - 1;
      setResendAttempts(newAttempts);
      localStorage.setItem(`${email}_resendAttempts`, newAttempts);

      // If attempts reach 0, start cooldown
      if (newAttempts === 0) {
        const cooldownEndTime = Math.floor(Date.now() / 1000) + 3600; // Set cooldown for 1 hour
        setCooldown(3600);
        localStorage.setItem(`${email}_cooldown`, cooldownEndTime);
      }
    } else {
      alert('You have exhausted your resend attempts. Please wait for cooldown to reset.');
    }
  };

  // Timer display
  const formatCooldown = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes}:${secs < 10 ? `0${secs}` : secs}`;
  };

  return (
    <div className="flex items-center justify-center h-screen bg-white">
      <div className="bg-white p-8 rounded-lg w-full max-w-xl">
        <h1 className="text-3xl font-semibold text-center mb-3">Enter Verification Code</h1>
        <h4 className="text-sm font-normal text-center mb-4 text-grey">
          We've sent a 6-digit code to your email
        </h4>

        <div className="flex justify-center mb-4">
          {code.map((digit, index) => (
            <input
              key={index}
              id={`input-${index}`}
              type="text"
              inputMode="numeric"
              maxLength={1}
              value={digit}
              onChange={(e) => handleChange(e, index)}
              className="w-12 h-12 mx-1 text-center text-2xl border border-gray-300 rounded-lg"
              autoComplete="off"
            />
          ))}
        </div>

        <button
          type="button"
          onClick={handleVerifyCode}
          className="w-full bg-black text-white p-3 rounded-lg mb-4"
        >
          Verify Code
        </button>

        <div className="mt-4 text-center">
          <p className="text-sm text-gray-600">
            Didn't get the code?{' '}
            <button
              onClick={handleResendCode}
              className={`text-blue-500 hover:underline cursor-pointer ${cooldown > 0 ? 'disabled' : ''}`}
              disabled={cooldown > 0}
            >
              Resend Code
            </button>
          </p>
          <p className="text-sm text-gray-600">
            Resend attempts left: {resendAttempts}
          </p>
          {cooldown > 0 && (
            <p className="text-red-500">
              Please wait {formatCooldown(cooldown)} before resending.
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
