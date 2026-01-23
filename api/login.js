import { MongoClient } from "mongodb";

const uri = process.env.MONGODB_URI;
const client = new MongoClient(uri);

export default async function handler(req, res) {
  if (req.method !== "POST") return res.status(405).json({ error: "Method not allowed" });

  const { user, pass } = req.body;

  if (!user || !pass) return res.status(400).json({ error: "Missing fields" });

  try {
    await client.connect();
    const db = client.db("claudplay");
    const users = db.collection("users");

    const found = await users.findOne({ user, pass });
    if (!found) return res.status(401).json({ error: "Invalid credentials" });

    return res.status(200).json({ ok: true });

  } catch (err) {
    return res.status(500).json({ error: err.message });
  }
}
