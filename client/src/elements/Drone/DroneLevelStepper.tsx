type DroneLevelStepperProps = {
  value: number;
  min: number;
  max: number;
  onChange: (value: number) => void;
};

export function DroneLevelStepper({
  value,
  min,
  max,
  onChange,
}: DroneLevelStepperProps) {
  return (
    <div className="flex items-center gap-2">
      <button
        className="w-7 h-7 rounded-lg border border-white/15 bg-white/5 text-white/60 hover:bg-white/10 hover:text-white/90 transition-all flex items-center justify-center text-base"
        onClick={() => onChange(Math.max(min, value - 1))}
      >
        −
      </button>
      <span className="text-sm font-medium text-white/85 min-w-8 text-center">
        {value}
      </span>
      <button
        className="w-7 h-7 rounded-lg border border-white/15 bg-white/5 text-white/60 hover:bg-white/10 hover:text-white/90 transition-all flex items-center justify-center text-base"
        onClick={() => onChange(Math.min(max, value + 1))}
      >
        +
      </button>
    </div>
  );
}
