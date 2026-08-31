interface InfoRowProps {
  label: string;
  value?: string | number | null;
}

const InfoRow: React.FC<InfoRowProps> = ({ label, value }) => {
  if (!value) {
    return null;
  }
  return (
    <div>
      <span className="text-sm font-semibold text-gray-500">{label}:</span>
      <p className="text-base text-black">{value}</p>
    </div>
  );
};
