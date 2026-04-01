/*
  Warnings:

  - You are about to drop the column `statKey` on the `SkillEffect` table. All the data in the column will be lost.
  - Added the required column `statKeyId` to the `SkillEffect` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE "SkillEffect" DROP COLUMN "statKey",
ADD COLUMN     "statKeyId" INTEGER NOT NULL;

-- CreateTable
CREATE TABLE "StatKey" (
    "id" SERIAL NOT NULL,
    "key" TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "category" TEXT NOT NULL,
    "valueType" TEXT NOT NULL,

    CONSTRAINT "StatKey_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "OverlordClass" (
    "id" SERIAL NOT NULL,
    "level" INTEGER NOT NULL,
    "className" TEXT NOT NULL,
    "overlordAtk" INTEGER NOT NULL,
    "overlordDef" INTEGER NOT NULL,
    "overlordHp" INTEGER NOT NULL,
    "overlordHpBoost" DOUBLE PRECISION,
    "overlordAtkBoost" DOUBLE PRECISION,
    "overlordDefBoost" DOUBLE PRECISION,

    CONSTRAINT "OverlordClass_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "OverlordSkill" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,
    "description" TEXT,
    "maxStars" INTEGER NOT NULL DEFAULT 10,

    CONSTRAINT "OverlordSkill_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "DroneComponent" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "maxLevel" INTEGER NOT NULL DEFAULT 12,
    "stat1Key" TEXT NOT NULL,
    "stat2Key" TEXT,

    CONSTRAINT "DroneComponent_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "UnitTroopLevel" (
    "id" SERIAL NOT NULL,
    "troopLevel" INTEGER NOT NULL,
    "unitPower" INTEGER NOT NULL,

    CONSTRAINT "UnitTroopLevel_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "TechProfile" (
    "id" SERIAL NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "TechProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "TechValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "TechValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "DecorationProfile" (
    "id" SERIAL NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "DecorationProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "DecorationValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "DecorationValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "CosmeticProfile" (
    "id" SERIAL NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "CosmeticProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "CosmeticValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "CosmeticValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "DroneProfile" (
    "id" SERIAL NOT NULL,
    "droneLevel" INTEGER NOT NULL,
    "combatBoostLevel" INTEGER NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "DroneProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "DroneValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "DroneValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "OverlordProfile" (
    "id" SERIAL NOT NULL,
    "level" INTEGER NOT NULL,
    "rank" TEXT NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "OverlordProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "OverlordValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "OverlordValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "UnitProfile" (
    "id" SERIAL NOT NULL,
    "troopLevel" INTEGER NOT NULL,
    "quantity" INTEGER NOT NULL,
    "morale" DOUBLE PRECISION NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "UnitProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "UnitValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "UnitValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "WallOfHonorProfile" (
    "id" SERIAL NOT NULL,
    "airCount" INTEGER NOT NULL,
    "airLevel" INTEGER NOT NULL,
    "tankCount" INTEGER NOT NULL,
    "tankLevel" INTEGER NOT NULL,
    "missileCount" INTEGER NOT NULL,
    "missileLevel" INTEGER NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "WallOfHonorProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "WallOfHonorValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "WallOfHonorValue_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "TacticsCardProfile" (
    "id" SERIAL NOT NULL,
    "power" DOUBLE PRECISION NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "TacticsCardProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "TacticsCardValue" (
    "id" SERIAL NOT NULL,
    "profileId" INTEGER NOT NULL,
    "statKeyId" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,

    CONSTRAINT "TacticsCardValue_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "StatKey_key_key" ON "StatKey"("key");

-- CreateIndex
CREATE UNIQUE INDEX "OverlordClass_level_key" ON "OverlordClass"("level");

-- CreateIndex
CREATE UNIQUE INDEX "OverlordSkill_name_key" ON "OverlordSkill"("name");

-- CreateIndex
CREATE UNIQUE INDEX "DroneComponent_name_key" ON "DroneComponent"("name");

-- CreateIndex
CREATE UNIQUE INDEX "UnitTroopLevel_troopLevel_key" ON "UnitTroopLevel"("troopLevel");

-- AddForeignKey
ALTER TABLE "SkillEffect" ADD CONSTRAINT "SkillEffect_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "TechValue" ADD CONSTRAINT "TechValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "TechProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "TechValue" ADD CONSTRAINT "TechValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DecorationValue" ADD CONSTRAINT "DecorationValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "DecorationProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DecorationValue" ADD CONSTRAINT "DecorationValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "CosmeticValue" ADD CONSTRAINT "CosmeticValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "CosmeticProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "CosmeticValue" ADD CONSTRAINT "CosmeticValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DroneValue" ADD CONSTRAINT "DroneValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "DroneProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DroneValue" ADD CONSTRAINT "DroneValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "OverlordValue" ADD CONSTRAINT "OverlordValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "OverlordProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "OverlordValue" ADD CONSTRAINT "OverlordValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "UnitValue" ADD CONSTRAINT "UnitValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "UnitProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "UnitValue" ADD CONSTRAINT "UnitValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "WallOfHonorValue" ADD CONSTRAINT "WallOfHonorValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "WallOfHonorProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "WallOfHonorValue" ADD CONSTRAINT "WallOfHonorValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "TacticsCardValue" ADD CONSTRAINT "TacticsCardValue_profileId_fkey" FOREIGN KEY ("profileId") REFERENCES "TacticsCardProfile"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "TacticsCardValue" ADD CONSTRAINT "TacticsCardValue_statKeyId_fkey" FOREIGN KEY ("statKeyId") REFERENCES "StatKey"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
