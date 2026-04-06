import { BACKEND_URL } from "@/constants";
import type { Gear } from "@/types";

export const getGears: () => Promise<Gear[]> = async () => {
  const response = await fetch(`${BACKEND_URL}/gears`);
  return response.json();
};
