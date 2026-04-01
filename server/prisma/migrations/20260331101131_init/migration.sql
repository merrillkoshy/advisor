/*
  Warnings:

  - You are about to drop the column `stat1Key` on the `DroneComponent` table. All the data in the column will be lost.
  - You are about to drop the column `stat2Key` on the `DroneComponent` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE "DroneComponent" DROP COLUMN "stat1Key",
DROP COLUMN "stat2Key";

-- CreateTable
CREATE TABLE "DroneComponentStatKey" (
    "id" SERIAL NOT NULL,
    "droneComponentId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,

    CONSTRAINT "DroneComponentStatKey_pkey" PRIMARY KEY ("id")
);

-- AddForeignKey
ALTER TABLE "DroneComponentStatKey" ADD CONSTRAINT "DroneComponentStatKey_droneComponentId_fkey" FOREIGN KEY ("droneComponentId") REFERENCES "DroneComponent"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DroneComponentStatKey" ADD CONSTRAINT "DroneComponentStatKey_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
