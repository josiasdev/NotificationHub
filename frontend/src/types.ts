export type SensorType = 
  | 'AIR_TEMPERATURE'
  | 'AIR_HUMIDITY'
  | 'SOIL_MOISTURE'
  | 'WATER_RESERVOIR_LEVEL'
  | 'SILO_LEVEL'
  | 'EQUIPMENT_STATUS';

export type NotificationSeverity = 'INFO' | 'WARNING' | 'CRITICAL';
export type NotificationStatus = 'PENDING' | 'SENT' | 'FAILED' | 'DLQ_ROUTED';

export interface SensorEventDTO {
  eventId: string;
  farmId: string;
  deviceId: string;
  type: SensorType;
  value: number | string | null;
  unit: string | null;
  timestamp: string;
}

export interface NotificationDTO {
  id: string;
  eventId: string;
  farmId: string;
  deviceId: string;
  severity: NotificationSeverity;
  message: string;
  recipientPhone: string;
  status: NotificationStatus;
  retryCount: number;
  createdAt: string;
  sentAt?: string | null;
  lastErrorMessage?: string | null;
}
