'use client';
import { useState, useEffect } from 'react'; // Import useEffect
import { useRouter } from 'next/navigation';
import 'bootstrap/dist/css/bootstrap.min.css';

export default function SendVerificationCode() {
  const [email, setEmail] = useState('');
  const [alertVisible, setAlertVisible] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertType, setAlertType] = useState(''); // 'success' or 'danger'
  const router = useRouter();

  // Effect to retrieve email
  useEffect(() => {
    const storedEmail = localStorage.getItem('userEmail');
    if (storedEmail) {
      setEmail(storedEmail);
      setAlertMessage(`A verification code has been sent to ${storedEmail}.`);
      setAlertType('success');
      setAlertVisible(true);
    } else {
      router.push('/forgot-password'); // Redirect if email is not found
    }
  }, [router]);

  return (
    <div className="flex items-center justify-center h-screen bg-white">
      <div className="bg-white p-8 rounded-lg w-full max-w-2xl">
        <h1 className="text-3xl font-semibold text-center mb-10">Send Verification Code</h1>
        {/* Custom success message alert */}
        {alertVisible && alertType === 'success' && (
          <div className="mb-4 p-3 text-black bg-[#B8EBC8] rounded-lg text-center">
            {alertMessage}
            <button type="button" className="close" onClick={() => setAlertVisible(false)}>
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
        )}
        <div className="mb-4">
          <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
            Email
          </label>
          <input
            type="email"
            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={email}
            readOnly
          />
        </div>
        <button
          type="button"
          onClick={() => router.push('/verify-code')}
          className="w-full bg-black text-white p-3 rounded-lg"
        >
          Go to Verify Code
        </button>
      </div>
    </div>
  );
}
