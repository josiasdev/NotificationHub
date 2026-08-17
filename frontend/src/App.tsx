import { useState, useEffect, useCallback } from 'react';
import { Navbar } from './components/Navbar';
import { SensorGauges } from './components/SensorGauges';
import { EventSimulator } from './components/EventSimulator';
import { EventsList } from './components/EventsList';
import { NotificationsList } from './components/NotificationsList';
import type { SensorEventDTO, NotificationDTO } from './types';
import { RefreshCw } from 'lucide-react';

const API_BASE_URL = 'http://localhost:8080';

export function App() {
  const [events, setEvents] = useState<SensorEventDTO[]>([]);
  const [notifications, setNotifications] = useState<NotificationDTO[]>([]);
  const [apiOnline, setApiOnline] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchData = useCallback(async () => {
    try {
      const [eventsRes, notifRes] = await Promise.all([
        fetch(`${API_BASE_URL}/api/v1/events`),
        fetch(`${API_BASE_URL}/api/v1/notifications`)
      ]);

      if (eventsRes.ok && notifRes.ok) {
        const eventsData: SensorEventDTO[] = await eventsRes.json();
        const notifData: NotificationDTO[] = await notifRes.json();

        setEvents(eventsData.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()));
        setNotifications(notifData.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()));
        setApiOnline(true);
      } else {
        setApiOnline(false);
      }
    } catch (err) {
      setApiOnline(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 3000);
    return () => clearInterval(interval);
  }, [fetchData]);

  const handleManualRefresh = async () => {
    setLoading(true);
    await fetchData();
    setLoading(false);
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '0 20px 40px' }}>
      <Navbar apiOnline={apiOnline} />

      <main>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 800 }}>Dashboard de Monitoramento IoT</h2>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)' }}>
              Painel em tempo real de ingestão de eventos e disparo de alertas da Fazenda Boa Esperança
            </p>
          </div>

          <button onClick={handleManualRefresh} disabled={loading} className="btn btn-secondary">
            <RefreshCw className={loading ? 'animate-spin' : ''} size={16} />
            Atualizar Dados
          </button>
        </div>

        <SensorGauges events={events} />

        <EventSimulator onEventIngested={fetchData} apiBaseUrl={API_BASE_URL} />

        <EventsList events={events} />

        <NotificationsList notifications={notifications} />
      </main>
    </div>
  );
}

export default App;
