import type { AdvisorRequest } from "@/dto";
import type { AdvisorResult } from "@/types";

import { apiPost } from "./client";

export async function analyze(request: AdvisorRequest) {
  return apiPost<AdvisorResult>(`/advisor/analyze`, request);
}
